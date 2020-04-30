import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { JobTypeComponentsPage, JobTypeDeleteDialog, JobTypeUpdatePage } from './job-type.page-object';

const expect = chai.expect;

describe('JobType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let jobTypeComponentsPage: JobTypeComponentsPage;
  let jobTypeUpdatePage: JobTypeUpdatePage;
  let jobTypeDeleteDialog: JobTypeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load JobTypes', async () => {
    await navBarPage.goToEntity('job-type');
    jobTypeComponentsPage = new JobTypeComponentsPage();
    await browser.wait(ec.visibilityOf(jobTypeComponentsPage.title), 5000);
    expect(await jobTypeComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.jobType.home.title');
    await browser.wait(ec.or(ec.visibilityOf(jobTypeComponentsPage.entities), ec.visibilityOf(jobTypeComponentsPage.noResult)), 1000);
  });

  it('should load create JobType page', async () => {
    await jobTypeComponentsPage.clickOnCreateButton();
    jobTypeUpdatePage = new JobTypeUpdatePage();
    expect(await jobTypeUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.jobType.home.createOrEditLabel');
    await jobTypeUpdatePage.cancel();
  });

  it('should create and save JobTypes', async () => {
    const nbButtonsBeforeCreate = await jobTypeComponentsPage.countDeleteButtons();

    await jobTypeComponentsPage.clickOnCreateButton();

    await promise.all([jobTypeUpdatePage.setNameInput('name')]);

    expect(await jobTypeUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');

    await jobTypeUpdatePage.save();
    expect(await jobTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await jobTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last JobType', async () => {
    const nbButtonsBeforeDelete = await jobTypeComponentsPage.countDeleteButtons();
    await jobTypeComponentsPage.clickOnLastDeleteButton();

    jobTypeDeleteDialog = new JobTypeDeleteDialog();
    expect(await jobTypeDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.jobType.delete.question');
    await jobTypeDeleteDialog.clickOnConfirmButton();

    expect(await jobTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
